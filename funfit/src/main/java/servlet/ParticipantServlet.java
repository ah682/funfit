package servlet;

import model.Participant;
import dao.ParticipantDAO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/participants")
public class ParticipantServlet extends HttpServlet {
    private ParticipantDAO participantDAO = new ParticipantDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Fetch all participants from the database
            req.setAttribute("participants", participantDAO.getAllParticipants());

            // Forward to the JSP for rendering
            RequestDispatcher dispatcher = req.getRequestDispatcher("participants.jsp");
            dispatcher.forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching participants.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");

            if (action == null || action.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing or invalid.");
                return;
            }

            if ("add".equals(action)) {
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                int batchId = Integer.parseInt(req.getParameter("batchId"));

                Participant newParticipant = new Participant();
                newParticipant.setName(name);
                newParticipant.setEmail(email);
                newParticipant.setBatchId(batchId);

                boolean isAdded = participantDAO.addParticipant(newParticipant);

                if (isAdded) {
                    resp.sendRedirect("participants");
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to add participant.");
                }
            } else if ("edit".equals(action)) {
                String participantIdParam = req.getParameter("participantId");
                if (participantIdParam == null || participantIdParam.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Participant ID is missing or invalid.");
                    return;
                }

                int participantId = Integer.parseInt(participantIdParam);
                Participant participant = participantDAO.getParticipantById(participantId);

                if (participant == null) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Participant not found with ID: " + participantId);
                    return;
                }

                req.setAttribute("editMode", true);
                req.setAttribute("editParticipantId", participant.getParticipantId());
                req.setAttribute("editParticipantName", participant.getName());
                req.setAttribute("editParticipantEmail", participant.getEmail());
                req.setAttribute("editParticipantBatchId", participant.getBatchId());

                req.setAttribute("participants", participantDAO.getAllParticipants());
                RequestDispatcher dispatcher = req.getRequestDispatcher("participants.jsp");
                dispatcher.forward(req, resp);
            } else if ("update".equals(action)) {
                String participantIdParam = req.getParameter("participantId");
                if (participantIdParam == null || participantIdParam.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Participant ID is missing or invalid.");
                    return;
                }

                int participantId = Integer.parseInt(participantIdParam);
                String name = req.getParameter("name");
                String email = req.getParameter("email");
                int batchId = Integer.parseInt(req.getParameter("batchId"));

                Participant updatedParticipant = new Participant();
                updatedParticipant.setParticipantId(participantId);
                updatedParticipant.setName(name);
                updatedParticipant.setEmail(email);
                updatedParticipant.setBatchId(batchId);

                boolean isUpdated = participantDAO.updateParticipant(updatedParticipant);

                if (isUpdated) {
                    resp.sendRedirect("participants");
                } else {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update participant.");
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action: " + action);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Error processing participant data.");
        }
    }
}
