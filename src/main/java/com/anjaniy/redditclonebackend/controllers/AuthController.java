package com.anjaniy.redditclonebackend.controllers;

import com.anjaniy.redditclonebackend.dto.*;
import com.anjaniy.redditclonebackend.models.User;
import com.anjaniy.redditclonebackend.repositories.UserRepo;
import com.anjaniy.redditclonebackend.services.AuthService;
import com.anjaniy.redditclonebackend.services.RefreshTokenService;
import com.anjaniy.redditclonebackend.utilities.SubredditExcelExporter;
import com.anjaniy.redditclonebackend.utilities.UserExcelExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
@CrossOrigin(origins = {"http://localhost:4200"})

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication REST Endpoint")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    private final UserRepo userRepo;

    @PostMapping("/signup")
    @Operation(summary = "Endpoint For User Registration.")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {

        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration Success", OK);
    }

    @Operation(summary = "Endpoint For User Account Verification.")
    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {

        authService.verifyAccount(token);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @Operation(summary = "Endpoint For User Login.")
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {

        return authService.login(loginRequest);
    }

    @Operation(summary = "Endpoint For Refresh Token.")
    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {

        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    @Operation(summary = "Endpoint For User Logout.")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh Token Deleted Successfully!!");
    }

    @GetMapping("/signup")
    @Operation(summary = "Endpoint To Get All The Subreddits.")
    public ResponseEntity<List<User>> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK).body(authService.getAll());
    }

    @Transactional(readOnly = true)
    @GetMapping("/signup/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_category";
        response.setHeader(headerKey, headerValue);

        List<User> userList = userRepo.findAll();

        UserExcelExporter excelExporter = new UserExcelExporter(
                userList);

        excelExporter.export(response);
    }


}
