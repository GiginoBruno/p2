package it.unisa.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unisa.model.*;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDao usDao = new UserDao();

        try {
            UserBean user = new UserBean();
            user.setUsername(sanitizeInput(request.getParameter("un")));
            user.setPassword(sanitizeInput(request.getParameter("pw")));
            user = usDao.doRetrieve(user.getUsername(), user.getPassword());

            String checkout = sanitizeInput(request.getParameter("checkout"));

            if (user.isValid()) {
                HttpSession session = request.getSession(true);
                session.setAttribute("currentSessionUser", user);
                if (checkout != null)
                    response.sendRedirect(request.getContextPath() + "/account?page=Checkout.jsp");
                else
                    response.sendRedirect(request.getContextPath() + "/Home.jsp");
            } else
                response.sendRedirect(request.getContextPath() + "/Login.jsp?action=error"); //error page
        } catch (SQLException e) {
            System.out.println("Error:" + e.getMessage());
        }
    }

    // Metodo per sanificare l'input utente
    private String sanitizeInput(String input) {
        // Implementa il tuo metodo di sanificazione qui
        // Ad esempio, puoi utilizzare un filtro per rimuovere caratteri non consentiti o utilizzare librerie come OWASP Java Encoder
        // Ecco un esempio di base per rimuovere i tag HTML:
        if (input != null) {
            return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        return null ;
    }
}
