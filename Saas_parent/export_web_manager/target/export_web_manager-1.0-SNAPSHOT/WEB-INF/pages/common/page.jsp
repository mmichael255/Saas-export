<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<body>
<div class="pull-left">
    <div class="form-group form-inline">
        总共${PageInfo.pages} 页，共${PageInfo.total} 条数据。
    </div>
</div>

<div class="box-tools pull-right">
    <ul class="pagination" style="margin: 0px;">
        <li >
            <a href="javascript:goPage(1)" aria-label="Previous">首页</a>
        </li>
        <li><a href="javascript:goPage(${PageInfo.prePage})">上一页</a></li>
        <c:forEach begin="${PageInfo.pageNum-5 > 0?PageInfo.pageNum-5:'1' }" end="${PageInfo.pageNum+5<PageInfo.pages?PageInfo.pageNum+5:PageInfo.pages}" var="i">
            <li class="paginate_button ${PageInfo.pageNum==i ? 'active':''}"><a href="javascript:goPage(${i})">${i}</a></li>
        </c:forEach>
        <li><a href="javascript:goPage(${PageInfo.nextPage})">下一页</a></li>
        <li>
            <a href="javascript:goPage(${PageInfo.pages})" aria-label="Next">尾页</a>
        </li>
    </ul>
</div>
<form id="pageForm" action="${param.pageUrl}" method="post">
    <input type="hidden" name="pageNum" id="pageNum">
</form>
<script>
    function goPage(page) {
        document.getElementById("pageNum").value = page
        document.getElementById("pageForm").submit()
    }
</script>
</body>
</html>
