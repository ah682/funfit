<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Participant" %>

<!DOCTYPE html>
<html>
<head>
    <title>Participants</title>
</head>
<body>
    <h1>Participants List</h1>

    <table border="1">
        <thead>
            <tr>
                <th>Participant ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Batch ID</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
            List<model.Participant> participants = (List<model.Participant>) request.getAttribute("participants");
            for (model.Participant participant : participants) { 
            %>
            <tr>
                <td><%= participant.getParticipantId() %></td>
                <td><%= participant.getName() %></td>
                <td><%= participant.getEmail() %></td>
                <td><%= participant.getBatchId() %></td>
                <td>
                    <form action="/funfit/participants" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="participantId" value="<%= participant.getParticipantId() %>">
                        <button type="submit">Edit</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </tbody>
    </table>

    <hr>

    <h1><%= request.getAttribute("editMode") != null ? "Edit Participant" : "Add New Participant" %></h1>
    <form action="/funfit/participants" method="post">
        <input type="hidden" name="action" value="<%= request.getAttribute("editMode") != null ? "update" : "add" %>">
        <% if (request.getAttribute("editMode") != null) { %>
            <input type="hidden" name="participantId" value="<%= request.getAttribute("editParticipantId") %>">
        <% } %>
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="<%= request.getAttribute("editParticipantName") != null ? request.getAttribute("editParticipantName") : "" %>" required>
        <br><br>
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" value="<%= request.getAttribute("editParticipantEmail") != null ? request.getAttribute("editParticipantEmail") : "" %>" required>
        <br><br>
        <label for="batchId">Batch ID:</label>
        <input type="number" id="batchId" name="batchId" value="<%= request.getAttribute("editParticipantBatchId") != null ? request.getAttribute("editParticipantBatchId") : "1" %>" required>
        <br><br>
        <button type="submit"><%= request.getAttribute("editMode") != null ? "Update Participant" : "Add Participant" %></button>
    </form>
</body>
</html>
