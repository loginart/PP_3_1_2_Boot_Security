$(document).ready(function() {
    loadUsers(); // Загрузка пользователей при загрузке страницы

    // Функция для загрузки пользователей
    function loadUsers() {
        fetch('/api/users')
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(users => {
                populateUserTable(users);
            })
            .catch(error => console.error('Ошибка при загрузке пользователей:', error));
    }

    // Функция для заполнения таблицы пользователей
    function populateUserTable(users) {
        const tableBody = $('#userTableBody');
        tableBody.empty();

        users.forEach(user => {
            // Преобразуем роли в строки
            const rolesText = user.roles.map(role => role.role).join(', ');

            const row = `
            <tr>
                <td>${user.id}</td>
                <td>${user.name}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${rolesText}</td>
                <td>
                    <button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#editUserModal"
                            data-user-id="${user.id}"
                            data-user-name="${user.name}"
                            data-user-lastname="${user.lastName}"
                            data-user-age="${user.age}"
                            data-user-roles="${user.roles.map(r => r.role).join(',')}">
                        Редактировать
                    </button>
                </td>
                <td>
                    <form action="/admin/delete?id=${user.id}" method="post" style="display:inline;">
                        <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Вы уверены, что хотите удалить этого пользователя?');">Удалить</button>
                    </form>
                </td>
            </tr>
        `;
            tableBody.append(row);
        });
    }

    // Открытие модального окна редактирования пользователя
    $('#editUserModal').on('show.bs.modal', function(event) {
        const button = $(event.relatedTarget);
        const modal = $(this);

        modal.find('#editUserId').val(button.data('user-id'));
        modal.find('#editUserName').val(button.data('user-name'));
        modal.find('#editUserLastName').val(button.data('user-lastname'));
        modal.find('#editUserAge').val(button.data('user-age'));
        modal.find('#editUserPassword').val('');

        // Устанавливаем выбранные роли
        const roles = button.data('user-roles').split(',');
        modal.find('#editUserRoles').val(roles);
    });

    // Обработка отправки формы редактирования пользователя
    $('#editUserForm').on('submit', function(event) {
        event.preventDefault();
        const userId = $('#editUserId').val();
        const userData = {
            name: $('#editUserName').val(),
            lastName: $('#editUserLastName').val(),
            age: $('#editUserAge').val(),
            password: $('#editUserPassword').val()
        };
        const roles = $('#editUserRoles').val();

        // Создаем параметры запроса
        const queryParams = new URLSearchParams();
        roles.forEach(role => queryParams.append('roles', role));

        fetch(`/api/users/${userId}?${queryParams}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        })
            .then(response => {
                if (response.ok) {
                    loadUsers();
                    $('#editUserModal').modal('hide');
                } else {
                    console.error('Ошибка при обновлении пользователя');
                }
            })
            .catch(error => console.error('Ошибка:', error));
    });
});