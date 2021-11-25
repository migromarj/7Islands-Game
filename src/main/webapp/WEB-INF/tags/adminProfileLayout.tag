<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="sevenislands" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ attribute name="pageName" required="true" %>
<%@ attribute name="customScript" required="false" fragment="true"%>
<%@ attribute name="subtitle" required="false"%>

<!doctype html>
<html>
<sevenislands:htmlHeader/>

<body>
<sevenislands:bodyHeader menuName="${pageName}"/>


<div id="body-section">

    <div id="top-section">
        <a href="/admins/profile/${admin.id}">
            <img id="profile-avatar" src="/resources/images/profile-photo.png">
        </a>
        <a href="/admins/profile/${admin.id}">
            <h2><c:out value="${admin.user.username}"/></h2>
        </a>
        <h2 id="admin-tag">Admin</h2>

        <h2 id="title"><c:out value="${subtitle}"/></h2>
    </div>

    <c:if test="${not empty message}" >
        <div class="alert alert-${not empty messageType ? messageType : 'info'}" role="alert">
            <c:out value="${message}"></c:out>
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button> 
        </div>
    </c:if>

    <div id="mid-section">
        <jsp:doBody/>
    </div>

    <div id="bottom-section"></div>

</div>

<sevenislands:footer/>
<jsp:invoke fragment="customScript" />

</body>

</html>