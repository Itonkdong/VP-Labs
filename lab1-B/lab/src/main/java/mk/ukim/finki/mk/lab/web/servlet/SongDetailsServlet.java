package mk.ukim.finki.mk.lab.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.mk.lab.model.Song;
import mk.ukim.finki.mk.lab.service.SongService;
import mk.ukim.finki.mk.lab.service.helper.CustomHandler;
import mk.ukim.finki.mk.lab.service.helper.WebContextBuilder;
import org.apache.tomcat.util.buf.CharsetUtil;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "songDetails", urlPatterns = "/songDetails")
public class SongDetailsServlet extends HttpServlet
{

    private final SongService songService;
    private final SpringTemplateEngine engine;

    public SongDetailsServlet(SongService songService, SpringTemplateEngine engine)
    {
        this.songService = songService;
        this.engine = engine;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String trackId = req.getParameter("trackId");

        if (CustomHandler.isNullOrEmpty(trackId))
        {
            resp.sendRedirect("/listSongs");
            return;
        }

        Optional<Song> song = songService.findByTrackId(trackId);

        if (song.isEmpty())
        {
            resp.sendRedirect("/listSongs");
            return;
        }

        WebContext context = WebContextBuilder.getContext(req, resp, this.getServletContext());
        context.setVariable("song", song.get());

        engine.process("songDetails.html", context, resp.getWriter());

    }
}
