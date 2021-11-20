<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="SevenIslands" tagdir="/WEB-INF/tags" %>

<SevenIslands:layout pageName="games">
    <h2>RoomsCreateByMe</h2>

    <table id="gamesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 200px;">Games</th>
            <th style="width: 200px;">Date</th>
            <th style="width: 200px;">Players</th>
           
        </tr>
        </thead>
        <tbody>
            
        <c:forEach items="${games}" var="game">
                <tr>
                    <td>
                        <c:out value="${game.name}"/>
                    </td>
    
                    <td>
                        <c:out value="${game.startTime}"/>
                    </td> 
    
                    <td>
                    <c:forEach items ="${game.players}" var="p">
                    
                    <c:out value = "${p.user.username}"/>  <!--AQUI SE PUEDE PONER LO DE DIRECCIONAR A LA VISTA DE UN PERFIL DANDOLE EL ID-->
                    
        
                    </c:forEach>  
                    </td>  
                
                    
                </tr>
            </c:forEach>


        </tbody>
    </table>
    
    <h2>RoomsWhereIPlayed</h2>
    <table id="gamesTable" class="table table-striped">
        <thead>
        <tr>
            <th style="width: 200px;">Games</th>
            <th style="width: 200px;">Date</th>
            <th style="width: 200px;">Players</th>
           
        </tr>
        </thead>
        <tbody>
            <c:forEach items="${games2}" var="game">
                <tr>
                    <td>
                        <c:out value="${game.name}"/>
                    </td>
    
                    <td>
                        <c:out value="${game.startTime}"/>
                    </td> 
    
                    <td>
                    <c:forEach items ="${game.players}" var="p">
                    
                    <c:out value = "${p.user.username}"/>  <!--AQUI SE PUEDE PONER LO DE DIRECCIONAR A LA VISTA DE UN PERFIL DANDOLE EL ID-->
                    
        
                    </c:forEach>  
                    </td>  
                
                    
                </tr>
            </c:forEach>

        </tbody>
    </table>
</SevenIslands:layout>
