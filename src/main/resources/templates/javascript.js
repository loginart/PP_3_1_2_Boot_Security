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
        tableBody.empty(); // Очищаем таблицу перед добавлением новых данных

        users.forEach(user => {
            const row = `
                <tr>
                    <td>${user.id}</td>
                    <td>${user.name}</td>
                    <td>${user.lastName}</td>
                    <td>${user.age}</td>
                    <td>${user.roles.join(', ')}</td>
                    <td>
                        <button type="button" class="btn btn-warning btn-sm" data-toggle="modal" data-target="#editUserModal"
                                data-user-id="${user.id}"
                                data-user-name="${user.name}"
                                data-user-lastname="${user.lastName}"
                                data-user-age="${user.age}"
                                data-user-roles="${user.roles.join(', ')}">
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
        const button = $(event.relatedTarget); // Кнопка, которая открыла модальное окно
        const userId = button.data('user-id'); // Получаем ID пользователя
        const userName = button.data('user-name'); // Получаем имя пользователя
        const userLastName = button.data('user-lastname'); // Получаем фамилию пользователя
        const userAge = button.data('user-age'); // Получаем возраст пользователя
        const userRoles = button.data('user-roles'); // Получаем роли пользователя

        // Заполняем поля формы
        const modal = $(this);
        modal.find('#editUserId').val(userId);
        modal.find('#editUserName').val(userName);
        modal.find('#editUserLastName').val(userLastName);
        modal.find('#editUserAge').val(userAge);
        modal.find('#editUserPassword').val(''); // Оставляем поле пароля пустым
        modal.find('#editUserRoles').val(userRoles.split(', ')); // Устанавливаем выбранные роли
    });

    // Обработка отправки формы редактирования пользователя
    $('#editUserForm').on('submit', function(event) {
        event.preventDefault();
        const userId = $('#editUserId').val();
        const userData = {
            id: userId,
            name: $('#editUserName').val(),
            lastName: $('#editUserLastName').val(),
            age: $('#editUserAge').val(),
            password: $('#editUserPassword').val(),
            roles: $('#editUserRoles').val()
        };

        fetch(`/api/users/${userId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(userData)
        })
            .then(response => {
                if (response.ok) {
                    loadUsers(); // Перезагружаем список пользователей
                    $('#editUserModal').modal('hide'); // Закрываем модальное окно
                } else {
                    console.error('Ошибка при обновлении пользователя');
                }
            })
            .catch(error => console.error('Ошибка:', error));
    });
});