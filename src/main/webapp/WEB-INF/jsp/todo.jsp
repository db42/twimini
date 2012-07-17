<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <style>
        form {
            display: inline-block;
        }
    </style>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
    <script src="/static/js/ejs_production.js"></script>

    <script type="text/javascript">
        function submitForm(id) {
            var name = '#' + id;
            $(name).submit();
        }

        function addToDoItem(form) {
            $.post('/todo/new.json',
                    $(form).serialize(), function (data) {
                        var todoItemLI = $(new EJS({url:'/static/ejs/foo.ejs'}).render(data));
                        $('#todoList').append(todoItemLI);

                    });
        }

        function deleteToDoItem(id) {
            var form_id= "#delete-"+id;
            var form= $(form_id);

            $.post('/todo/delete.json',
                    $(form).serialize(), function (data) {
                        var li_id= '#li-'+data.index;
                        $(li_id).remove();

                    });
        }

        function editToDoItem(form) {
            $.post('/todo/edit.json',
                    $(form).serialize(), function (data) {
                        var todoItemLI = $(new EJS({url:'/static/ejs/foo.ejs'}).render(data));
                        var li_id= '#li-'+data.index;
                        $(li_id).replaceWith(todoItemLI);

                    });
        }


    </script>

</head>
<body>

<h2>ToDo</h2>
<ul id="todoList">

    <c:forEach items="${todo_new}" var="obj" varStatus="count">
        <li id="li-<c:out value="${count.index}"/>">
            <form id="delete-<c:out value="${count.index}"/>" action="" method="post"  "> todo <c:out value="${obj}"/> |
                <input type="hidden" name="index" value="<c:out value="${count.index}"/>"/>
                <a href="javascript:deleteToDoItem(<c:out value='${count.index}'/>); return false;">Delete</a>|
            </form>

            <form id="edit-<c:out value="${count.index}"/>" action="" method="post" onsubmit="editToDoItem(this); return false;">
                <input type="hidden" name="index_new" value="<c:out value="${count.index}"/>"/>
                <input type="text" name="todo_old_change" value="<c:out value="${obj}"/>"/>
                <input type="submit" value="Save Changes"/>
            </form>

        </li>
    </c:forEach>
</ul>

<form action="" method="post" onsubmit="addToDoItem(this); return false;">
    New todo : <input type="text" name="todo_new"/>
    <input type="submit" value="Add"/>
</form>
</body>
</html>
