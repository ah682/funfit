<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Batch" %>

<!DOCTYPE html>
<html>
<head>
    <title>Batches</title>
</head>
<body>
    <h1>Batches List</h1>

    <table border="1">
        <thead>
            <tr>
                <th>Batch ID</th>
                <th>Batch Name</th>
                <th>Timing</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
            List<Batch> batches = (List<Batch>) request.getAttribute("batches");
            if (batches != null) {
                for (Batch batch : batches) { 
            %>
            <tr>
                <td><%= batch.getBatchId() %></td>
                <td><%= batch.getBatchName() %></td>
                <td><%= batch.getTiming() %></td>
                <td>
                    <form action="/funfit/batches" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="batchId" value="<%= batch.getBatchId() %>">
                        <button type="submit">Edit</button>
                    </form>
                </td>
            </tr>
            <% 
                }
            }
            %>
        </tbody>
    </table>

    <hr>

    <h1><%= request.getAttribute("editMode") != null ? "Edit Batch" : "Add New Batch" %></h1>
    <form action="/funfit/batches" method="post">
        <input type="hidden" name="action" value="<%= request.getAttribute("editMode") != null ? "update" : "add" %>">
        <% if (request.getAttribute("editMode") != null) { %>
            <input type="hidden" name="batchId" value="<%= request.getAttribute("editBatchId") %>">
        <% } %>
        <label for="batchName">Batch Name:</label>
        <input type="text" id="batchName" name="batchName" value="<%= request.getAttribute("editBatchName") != null ? request.getAttribute("editBatchName") : "" %>" required>
        <br><br>
        <label for="timing">Timing:</label>
        <input type="text" id="timing" name="timing" value="<%= request.getAttribute("editTiming") != null ? request.getAttribute("editTiming") : "" %>" required>
        <br><br>
        <button type="submit"><%= request.getAttribute("editMode") != null ? "Update Batch" : "Add Batch" %></button>
    </form>
</body>
</html>
