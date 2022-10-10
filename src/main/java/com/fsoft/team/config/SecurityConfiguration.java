package com.fsoft.team.config;

import com.fsoft.team.service.UserService;
import com.fsoft.team.serviceImp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServiceImp userService;

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(userService);
//        auth.setPasswordEncoder(passwordEncoder());
//        return auth;
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(authenticationProvider());
//    }
    @Bean
    public PasswordEncoder passwordEncoder(){

        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers(
                        "/users",
                        "/delete",
                        "/enroll/add",
                        "/enroll/remove",
                        "/enroll/update",
                        "/user/create",

                        "/categorylist",
                        "/deletecategory",
                        "/addcategoryform",
                        "/addcategory",
                        "/editcategoryform",
                        "/editcategory"
                )
                .hasAnyRole("SUPERADMIN", "ADMIN")
                .antMatchers(

                        "/delete-question",
                        "/editQuestion",
                        "/addQuestion",
                        "/saveOrUpdate",
                        "/reset-progress",

                        "/add-content-form",
                        "/add-webcontent-form",
                        "/add-quiz-form",
                        "/add-content",
                        "/delete-content",
                        "/add-webcontent",
                        "add-quiz",
                        "edit-quiz",
                        "add-section",
                        "delete-section",
                        "/addCourse",
                        "/addNewCourse",
                        "/unEnroll",
                        "/editUsers",
                        "/enroll",
                        "/addUserInCouser",
                        "/deleteEnrol",
                        "/courseList",
                        "/editCourse",
                        "/deleteCoure",
                        "/cloneCourse",
                        "/active",
                        "/deActive",
                        "/deleteCourses",
                        "/addQuestionByImport",
                        "/course-catalog"

                ).hasAnyRole("SUPERADMIN","INSTRUCTOR","ADMIN")
                .antMatchers(
                        "/course-catalog",
                        "/doContent",
                        "/complete-content",
                        "/next-content",
                        "/previous-content",
                        "/edit",
                        "/home-page",
                        "/start-this-course",
                        "/doQuiz",
                        "/startQuiz",
                        "/resultQuiz",
                        "/GenerateCerti",
                        "/addOrUpdateUser"
                ).hasAnyRole("LEANER", "SUPERADMIN","INSTRUCTOR","ADMIN")
                .anyRequest().permitAll()
                .and().formLogin().loginPage("/login").loginProcessingUrl("/checklogin")
                .usernameParameter("username").passwordParameter("password")
                .defaultSuccessUrl("/home-page", true)
                .failureUrl("/login?error=failed").and().exceptionHandling().accessDeniedPage("/403")
                .and()
                .logout().permitAll()
                ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }

}
