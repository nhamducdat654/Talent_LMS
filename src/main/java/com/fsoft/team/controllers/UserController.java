package com.fsoft.team.controllers;

import com.fsoft.team.dtos.CourseDTO;
import com.fsoft.team.dtos.EnrollementDTO;
import com.fsoft.team.dtos.UserDTO;
import com.fsoft.team.entity.Course;
import com.fsoft.team.entity.Enrollement;
import com.fsoft.team.entity.Role;
import com.fsoft.team.entity.User;
import com.fsoft.team.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes({"USER"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollementService enrollementService;

    @Autowired
    private ContentUserService contentUserService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuizUserService quizUserService;

    @GetMapping("/users")
    public String showListUser(Model model) {
        List<User> listUserEntity = userService.findAll();
        List<UserDTO> listUserDto = new ArrayList<>();
        for (int i = 0; i < listUserEntity.size(); i++) {
            listUserDto.add(new UserDTO(listUserEntity.get(i).getUsername(),
                    listUserEntity.get(i).getEmail(),
                    listUserEntity.get(i).getPassword(),
                    listUserEntity.get(i).getFirstName(),
                    listUserEntity.get(i).getLastName(),
                    listUserEntity.get(i).getBio(),
                    listUserEntity.get(i).getUserImg(),
                    listUserEntity.get(i).getCreateDate(),
                    listUserEntity.get(i).getStatus(),
                    listUserEntity.get(i).getRole().getRoleID(),
                    listUserEntity.get(i).getRole().getRoleName()));
        }
        model.addAttribute("LISTUSER", listUserDto);
        return "user/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("txtUsername") String username, Model model) {
        userService.disableUser(username);
        model.addAttribute("SUCCESS", "UPDATE SUCCESS");
        return "redirect:/users";
    }

    @GetMapping("/edit")
    public String editUserPage(@RequestParam(value = "txtId", required = false) String username, Model model) {
        User user = userService.findByUsername(username);
        UserDTO userDTO = new UserDTO(user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getBio(),
                user.getUserImg(),
                user.getCreateDate(),
                user.getStatus(),
                user.getRole().getRoleID(),
                user.getRole().getRoleName());
        model.addAttribute("USERDETAIL", userDTO);

        List<Course> listCourseEntity = courseService.findAllCourse();

        List<Enrollement> listEnrollementEntity = enrollementService.getListEnrollementByUsername(username);
        
        List<CourseDTO> listCourseDTO = new ArrayList<>();

        String listIdOfEnrollmentEntity = "";
        for (int i = 0; i < listEnrollementEntity.size(); i++) {
            listIdOfEnrollmentEntity += listEnrollementEntity.get(i).getCourse().getCourseID();
        }

        for (int i = 0; i < listCourseEntity.size(); i++) {
            if (listIdOfEnrollmentEntity.contains(listCourseEntity.get(i).getCourseID().toString())) {
                for (int j = 0; j < listEnrollementEntity.size(); j++) {
                    if (listCourseEntity.get(i).getCourseID().equals(listEnrollementEntity.get(j).getCourse().getCourseID())) {
                        listCourseDTO.add(
                                new CourseDTO(
                                        listCourseEntity.get(i).getCourseID(),
                                        listCourseEntity.get(i).getCourseDes(),
                                        listCourseEntity.get(i).getCourseImg(),
                                        listCourseEntity.get(i).getCourseName(),
                                        listCourseEntity.get(i).getDateOfCreate(),
                                        listCourseEntity.get(i).getPrice(),
                                        listCourseEntity.get(i).getStatus(),
                                        listEnrollementEntity.get(j).getEnrollDate(),
                                        listEnrollementEntity.get(j).getDateComplete(),
                                        listEnrollementEntity.get(j).getRole(),
                                        listEnrollementEntity.get(j).getUser().getUsername(),
                                        listEnrollementEntity.get(j).getCheckDisable(),
                                        listEnrollementEntity.get(j).getEnrollID(),
                                        listCourseEntity.get(i).getCategory().getCategoryID(),
                                        listCourseEntity.get(i).getUser().getUsername()
                                )
                        );

                    }
                }
            } else {
                listCourseDTO.add(
                        new CourseDTO(
                                listCourseEntity.get(i).getCourseID(),
                                listCourseEntity.get(i).getCourseDes(),
                                listCourseEntity.get(i).getCourseImg(),
                                listCourseEntity.get(i).getCourseName(),
                                listCourseEntity.get(i).getDateOfCreate(),
                                listCourseEntity.get(i).getPrice(),
                                listCourseEntity.get(i).getStatus(),
                                null,
                                null,
                                null,
                                null,
                                0,
                                null,
                                listCourseEntity.get(i).getCategory().getCategoryID(),
                                listCourseEntity.get(i).getUser().getUsername()
                        )
                );
            }

        }
        model.addAttribute("LISTCOURSE", listCourseDTO);

        return "user/edit";
    }

    @GetMapping("/enroll/add")
    public String addEnroll(@RequestParam("idCourse") Long idCourse, @RequestParam("username") String username, Model model) {
        User userEntity = userService.findByUsername(username);
        Optional<Course> courseOptional = courseService.findCourseByID(idCourse);
        Course courseEntity = courseOptional.get();
        LocalDateTime date_enroll = LocalDateTime.now();
        boolean defaultIsComplete = false;
        String role = userEntity.getRole().getRoleName();
        LocalDateTime date_complete = null;
        int defaultCheckDisable = 1;
        //create enrollment Entity
        Enrollement enrollementEntity = new Enrollement();
        enrollementEntity.setEnrollDate(date_enroll);
        enrollementEntity.setComplete(defaultIsComplete);
        enrollementEntity.setRole(role);
        enrollementEntity.setCourse(courseEntity);
        enrollementEntity.setUser(userEntity);
        enrollementEntity.setDateComplete(date_complete);
        enrollementEntity.setCheckDisable(defaultCheckDisable);
        enrollementService.save(enrollementEntity);

        return "redirect:/users";
    }

    @GetMapping("/enroll/remove")
    public String removeEnroll(@RequestParam("idEnroll") Long idEnroll) {
        int delete = enrollementService.delete(idEnroll);
        return "redirect:/users";
    }

    @GetMapping("/enroll/update")
    public String updateTimeComplete(@RequestParam("idEnroll") Long idEnroll, @RequestParam("courseID") Long courseID, @RequestParam("username") String username) {
        enrollementService.updateDateComplete(idEnroll);
        enrollementService.updateIsComplete(idEnroll);

        List<Long> listContentID = contentService.getContentIDByCourseID(courseID);
        for (Long long1 : listContentID) {
            contentUserService.deleteContentUserByContentIDAndUsername(long1, username);
        }

        List<Long> listQuizID = quizService.getQuizIDByCourseID(courseID);
        for (Long long1 : listQuizID) {
            quizUserService.deleteQuizUserByContentIDAndUsername(long1, username);
        }
        return "redirect:/users";
    }

//    @PostMapping("/checklogin")
//    public String checkLogin(ModelMap model, @RequestParam("username")String username,
//                             @RequestParam("password")String password, HttpSession session){
//        String url = "login";
//        String fullName = "";
//        User user = new User();
//        Optional<User> optionalUser;
//        if(userService.checkLogin(username,password) ){
//            optionalUser=userService.findById(username);
//            user=optionalUser.get();
//
//
//            session.setAttribute("USER",user);
//            url="redirect:/home-page";
//        }else{
//            if(userService.checkLoginByEmail(username,password)){
//                optionalUser=userService.findAllByEmail(username);
//                user=optionalUser.get();
//                fullName=user.getFirstName()+" "+user.getLastName();
//                session.setAttribute("FULLNAME",fullName);
//                session.setAttribute("USER",user);
//                url="redirect:/home-page";
//            }else{
//                model.addAttribute("LOGINFAIL","Username or password doesnt exist");
//                url="user/login";
//            }
//
//        }
//        return url;
//    }
    @GetMapping(value = {"/login", "/"})
    public String showLogin(@RequestParam(name = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("LOGINFAIL", "Invalid Username or Password");
        }
//            if (error.equals("failed")) {
//                model.addAttribute("LOGINFAIL", "Invalid Username or Password");
//            }

        return "user/login";
    }

    @GetMapping("/403")
    public String handleForbiden(Model model) {
        return "403";
    }

    @GetMapping("/user/create")
    public String createUserPage(Model model) {
        return "user/createUser";
    }

    @PostMapping("/addOrUpdateUser")
    public String addUser(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
            @RequestParam("email") String email, @RequestParam("image") MultipartFile image,
            @RequestParam("username") String username, @RequestParam("password") String password,
            @RequestParam("bio") String bio, @RequestParam("actionUser") String action,
            @RequestParam(value = "role", required = false) String addRole,
            ModelMap model) {
        String url = "";
        User userSession = (User) model.getAttribute("USER");
        System.out.println(userSession.getUsername() + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        try {
            User updateUser = null;
            String imageLink = "";
            String errorMessenger = "";
            boolean isExist = false;

            if (action.equals("CreateUser")) {
                url = "user/createUser";
                if (userService.checkEmailExist(email)) {
                    errorMessenger = "Email is already exist";
                    isExist = true;
                }
                if (userService.checkUsernameExist(username)) {
                    isExist = true;
                    if (errorMessenger.length() == 0) {
                        errorMessenger = "Username is already exist";
                    } else {
                        errorMessenger = "Email and Username are already exist";
                    }

                }
            } else {
                url = "user/edit";
                Optional<User> update = userService.findById(username);
                updateUser = update.get();
                if (!updateUser.getEmail().equals(email)) {
                    if (userService.checkEmailExist(email)) {
                        isExist = true;
                        errorMessenger = "Email is already exist";
                    }
                }
            }
            if (!isExist) {
                if (image.isEmpty()) {
                    imageLink = "default_user2x.png";
                } else {
                    Path path = Paths.get("../TalentLMSProjectSpringBoot/src/main/resources/static/img/");
                    try {
                        InputStream inputStream = image.getInputStream();
                        Files.copy(inputStream, path.resolve(image.getOriginalFilename()),
                                StandardCopyOption.REPLACE_EXISTING);
                        imageLink = image.getOriginalFilename();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                Role role = new Role();
                switch (addRole) {
                    case "SuperAdmin":
                        role.setRoleID("ROLE_SUPERADMIN");
                        role.setRoleName("Super Admin");
                        break;
                    case "Admin-Type":
                        role.setRoleID("ROLE_ADMIN");
                        role.setRoleName("Admin");
                        break;
                    case "Trainer-Type":
                        role.setRoleID("ROLE_INSTRUCTOR");
                        role.setRoleName("Instructor");
                        break;
                    case "Learner-Type":
                        role.setRoleID("ROLE_LEANER");
                        role.setRoleName("Leaner");
                        break;
                }
                User user = new User();
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                user.setUserImg(imageLink);
                user.setUsername(username);
                if (action.equals("UpdateUser") && password.length() == 0) {
                    System.out.println("lay password old");
                    user.setPassword(updateUser.getPassword());
                } else {
                    System.out.println("lay password trong input");
                    user.setPassword(password);
                }
                user.setBio(bio);
                user.setRole(role);
                user.setStatus(1);

                Date date = new Date();
                LocalDateTime localDate = date.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                user.setCreateDate(localDate);
                try {
                    userService.save(user);

                } catch (Exception e) {
                    System.out.println(e);
                }
                if (userSession.getRole().getRoleName().equals("Super Admin") || userSession.getRole().getRoleName().equals("Admin")) {
                    url = "redirect:/users";
                } else if (userSession.getRole().getRoleName().equals("Instructor") || userSession.getRole().getRoleName().equals("Leaner")) {
                    url = "redirect:/edit?txtId=" + userSession.getUsername();
                }

            } else {
                model.addAttribute("EXIST", errorMessenger);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return url;
    }

//    @GetMapping("/logout")
//    public String logoutTalentLMS(HttpServletRequest request) {
//
//        return "redirect:/login";
//    }
}
