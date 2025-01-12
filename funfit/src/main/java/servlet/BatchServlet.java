package servlet;

import model.Batch;
import dao.BatchDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/batches")
public class BatchServlet extends HttpServlet {
    private BatchDAO batchDAO = new BatchDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Fetch all batches
        req.setAttribute("batches", batchDAO.getAllBatches());
        RequestDispatcher dispatcher = req.getRequestDispatcher("batches.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");

            if ("add".equals(action)) {
                String batchName = req.getParameter("batchName");
                String timing = req.getParameter("timing");

                if (batchName == null || batchName.isEmpty() || timing == null || timing.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data.");
                    return;
                }

                Batch newBatch = new Batch();
                newBatch.setBatchName(batchName);
                newBatch.setTiming(timing);

                boolean isAdded = batchDAO.addBatch(newBatch);

                if (isAdded) {
                    resp.sendRedirect("batches");
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to add batch.");
                }
            } else if ("edit".equals(action)) {
                int batchId = Integer.parseInt(req.getParameter("batchId"));
                Batch batch = batchDAO.getAllBatches().stream()
                    .filter(b -> b.getBatchId() == batchId)
                    .findFirst()
                    .orElse(null);

                if (batch == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Batch not found.");
                    return;
                }

                req.setAttribute("editMode", true);
                req.setAttribute("editBatchId", batch.getBatchId());
                req.setAttribute("editBatchName", batch.getBatchName());
                req.setAttribute("editTiming", batch.getTiming());

                req.setAttribute("batches", batchDAO.getAllBatches());
                RequestDispatcher dispatcher = req.getRequestDispatcher("batches.jsp");
                dispatcher.forward(req, resp);
            } else if ("update".equals(action)) {
                int batchId = Integer.parseInt(req.getParameter("batchId"));
                String batchName = req.getParameter("batchName");
                String timing = req.getParameter("timing");

                if (batchName == null || batchName.isEmpty() || timing == null || timing.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input data.");
                    return;
                }

                Batch updatedBatch = new Batch();
                updatedBatch.setBatchId(batchId);
                updatedBatch.setBatchName(batchName);
                updatedBatch.setTiming(timing);

                boolean isUpdated = batchDAO.updateBatch(updatedBatch);

                if (isUpdated) {
                    resp.sendRedirect("batches");
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update batch.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing batch data.");
        }
    }

}
