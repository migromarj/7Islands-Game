<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ attribute name="name" required="true" rtexprvalue="true"
              description="Name of corresponding property in bean object" %>
<%@ attribute name="label" required="true" rtexprvalue="true"
              description="Label appears in red color if input is considered as invalid after submission" %>

<spring:bind path="${name}">
    <c:set var="cssGroup" value="form-group ${status.error ? 'has-error' : '' }"/>
    <c:set var="valid" value="${not status.error and not empty status.actualValue}"/>
    <div class="${cssGroup}">
        <label class="col-sm-2 control-label">${label}</label>
        <c:set var="label" value ="${label}"/>

        <div class="col-sm-10">
            <c:choose>
                <c:when test="${label == 'Password'}">
                    <form:input type = "Password" pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[ª@~$€!¡¿?|#º/\\<>{}+-])[A-Za-z\d@$!%*?&]{9,}$" title="Must contain at least one number and one uppercase and lowercase letter, and at least 9 or more characters" class="form-control" path="${name}"/>
                </c:when>
                <c:otherwise>
                    <form:input class="form-control" path="${name}"/>
                </c:otherwise>
            </c:choose>
            <c:if test="${valid}">
                <span class="glyphicon glyphicon-ok form-control-feedback" aria-hidden="true"></span>
            </c:if>
            <c:if test="${status.error}">
                <span class="glyphicon glyphicon-remove form-control-feedback" aria-hidden="true"></span>
                <span class="help-inline">${status.errorMessage}</span>
            </c:if>
        </div>
    </div>
</spring:bind>
