package mx.edu.utez.sda.springjwt.control;

import mx.edu.utez.sda.springjwt.entity.AuthRequest;
import mx.edu.utez.sda.springjwt.entity.UserInfo;
import mx.edu.utez.sda.springjwt.service.JwtService;
import mx.edu.utez.sda.springjwt.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserInfoService service;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @GetMapping("/index")
    public String index(){
        return "Servicio index";
    }
    @PostMapping("/register")
    public String register(@RequestBody UserInfo userInfo){
        return service.saveUser(userInfo);
    }
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String soloAdmin(){
        return "Este endpoint es solo para Admi";
    }
    @GetMapping("/user/test")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public String paraUser(){
        return "Este endpoint puede ser para Admin y User";
    }
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest){
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername() , authRequest.getPassword()));
            if(authentication.isAuthenticated()){
                return jwtService.generateToken(authRequest.getUsername());
            }else{
                System.out.println("No auntentico");
                throw new UsernameNotFoundException("Usuario invalido");
            }
        }catch (Exception e){
            System.out.println("Excepcion");
            throw new UsernameNotFoundException("Usuario invalido");
        }
    }
}
