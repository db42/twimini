<%@ page
        import="org.springframework.beans.propertyeditors.StringArrayPropertyEditor"
        import="java.util.ArrayList"
        %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>

<script type="text/javascript"
    src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js">
</script>

<script type="text/javascript"
        src="/static/js/ejs.js">
</script>

<script type="text/javascript">
    function TaskNew(form){
        $.post('/todo/new.json', $(form).serialize(),function(data) {
            var todoItemLI = $(new EJS({url:
            '/static/ejs/todo.ejs'
            }).render(data));
            $('#todoList').append(todoItemLI);
        });
    }

    function TaskDelete(index){
        $.get('/todo/delete.json?id='+index, function(data) {
            $('#'+index).remove();
        });
    }

    function TaskModify(form,index){
        $.post('/todo/modify.json',$(form).serialize(), function(data){
            var todoItemLI = $(new EJS({url:
                    '/static/ejs/todo.ejs'
            }).render(data));
            $('#'+index).replaceWith(todoItemLI);
        });
    }
</script>

</head>
<body>
<h1>ToDo</h1>

<ul id="todoList">
<c:set var="index" value="0" scope="page" />
<c:forEach var='item' items='${tasks}'>

    <li>

        <form name="modify" action="todo/modify" method="post">
        ${item} |<a href="todo/delete?id=${index}"> Delete</a>

        <input type="text" name="task_new" />
        <input type="hidden" name="id" value="${index}" />
        <input type="submit" value="Modify" />
        </form>
    </li>

    <c:set var="index" value="${index + 1}" scope="page"/>
</c:forEach>


</ul>

<form name="input" action="todo/new" method="post" onsubmit="TaskNew(this); return false;">
    New todo: <input type="text" name="task" />
    <input type="submit" value="Add" />
</form>
</body>
</html>