package org.example.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.advice.exceptions.AlreadyClickedLikeException;
import org.example.advice.exceptions.UserAlreadyExists;
import org.example.dto.ArticleDto;
import org.example.dto.MediumUserDto;
import org.example.mediumModels.*;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
@RequiredArgsConstructor
@Transactional
public class MediumController {
    private final SessionFactory session;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }


    @GetMapping("/pageSignup")
    public String pageSignup() {
        return "signup";
    }

    @GetMapping
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.setViewName("home2");
        return modelAndView;
    }

    @GetMapping("/showAllArticles")
    public ModelAndView showAll(ModelAndView modelAndView) {
        List<Article> selectAFromArticleA = session.getCurrentSession().createQuery("select a from Article a ", Article.class).list();
        modelAndView.addObject("list", selectAFromArticleA);
        modelAndView.setViewName("showAll");
        return modelAndView;
    }

    @GetMapping("/showUsers")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView showUsers(ModelAndView modelAndView) {
        List<UserMedium> selectUFromUserMediumU = session.getCurrentSession().createQuery("select u from UserMedium u ", UserMedium.class).list();
        modelAndView.addObject("users", selectUFromUserMediumU);
        modelAndView.setViewName("showUsers");
        return modelAndView;
    }

    @GetMapping("/articlePage")
    public String articlePage() {
        return "articlePage";
    }

    @PostMapping("/addArticle")
    public ModelAndView addArticle(ModelAndView modelAndView, @ModelAttribute ArticleDto articleDto, @RequestParam("user_email") String email) {
        Query<UserMedium> query = session.getCurrentSession().createQuery("select u from UserMedium u where email=:email", UserMedium.class)
                .setParameter("email", email);
        UserMedium userMedium = query.uniqueResult();

        session.getCurrentSession().persist(Article.builder().title(articleDto.title()).content(articleDto.content()).userMedium(userMedium).build());
        modelAndView.setViewName("home2");
        return modelAndView;
    }

    @PostMapping("/addArticle2")
    public ModelAndView addArticle2(ModelAndView modelAndView, @ModelAttribute ArticleDto articleDto, @RequestParam("user_email2") String email) {
        Query<UserMedium> query = session.getCurrentSession().createQuery("select u from UserMedium u where email=:email", UserMedium.class)
                .setParameter("email", email);
        UserMedium userMedium = query.uniqueResult();

        session.getCurrentSession().persist(Article.builder().title(articleDto.title()).content(articleDto.content()).userMedium(userMedium).build());
        modelAndView.setViewName("home2");
        return modelAndView;
    }

    @PostMapping("/signup")
    public ModelAndView signup(ModelAndView modelAndView, @ModelAttribute MediumUserDto mediumUserDto) {
        String email = checkEmail(mediumUserDto.email());
        if (email == null) {
            List<Role> roles = session.getCurrentSession().createQuery("select r from Role r where id=:id", Role.class).setParameter("id", 3).list();
            session.getCurrentSession().persist(UserMedium.builder().email(mediumUserDto.email()).password(mediumUserDto.password())
                    .name(mediumUserDto.username()).role(roles).build());
            modelAndView.addObject("email", mediumUserDto.email());
            modelAndView.setViewName("articlePage2");
            return modelAndView;
        } else {
            throw new UserAlreadyExists();
        }

    }

    @GetMapping("/{id}")
    public ModelAndView showArticle(ModelAndView modelAndView, @PathVariable("id") Long id) {
        Article article = session.getCurrentSession().get(Article.class, id);
        modelAndView.addObject("content", article);
        modelAndView.setViewName("showContent");
        return modelAndView;
    }

    @GetMapping("/like/{id}")
    public ModelAndView likeArticle(ModelAndView modelAndView, @PathVariable("id") Long id, @RequestParam("Email") String email,
                                    @RequestParam(value = "comment", required = false) String comment,
                                    @RequestParam(value = "check", required = false) String check) {

        Article article = session.getCurrentSession().get(Article.class, id);

        Long userId = session.getCurrentSession().createNativeQuery("select id from users where email=:email", Long.class)
                .setParameter("email", email).uniqueResult();

        addComment(comment, id, userId);

        Long idResult = session.getCurrentSession().createNativeQuery("select id from likes where user_id=:u_id and article_id=:a_id ", Long.class)
                .setParameter("u_id", userId)
                .setParameter("a_id", id)
                .uniqueResult();
        if (idResult != null) {
            throw new AlreadyClickedLikeException();
        } else {
            UserMedium userMedium = session.getCurrentSession().get(UserMedium.class, userId);
            if (check != null) {
                session.getCurrentSession().persist(Likes.builder().article(article).user(userMedium).build());
                modelAndView.setViewName("showContent");
            }
            return modelAndView;
        }

    }

    private void addComment(String comment, Long articleId, Long userId) {
        if (comment != null) {
            Article article = session.getCurrentSession().get(Article.class, articleId);

            UserMedium userMedium = session.getCurrentSession().get(UserMedium.class, userId);

            session.getCurrentSession().persist(Comments.builder().title(comment).article(article).user(userMedium).build());
        }
    }

    private String checkEmail(String email) {
        return session.getCurrentSession().createQuery("select email from UserMedium where email=:email", String.class)
                .setParameter("email", email).uniqueResult();
    }
}
