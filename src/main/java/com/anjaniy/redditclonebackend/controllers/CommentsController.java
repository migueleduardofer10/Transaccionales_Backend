package com.anjaniy.redditclonebackend.controllers;

import com.anjaniy.redditclonebackend.dto.CommentDto;
import com.anjaniy.redditclonebackend.dto.SubredditDto;
import com.anjaniy.redditclonebackend.services.CommentService;
import com.anjaniy.redditclonebackend.utilities.CommentExcelExporter;
import com.anjaniy.redditclonebackend.utilities.SubredditExcelExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
@CrossOrigin(origins = {"http://localhost:4200"})

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
@Tag(name = "Comment REST Endpoint")
public class CommentsController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @Operation(summary = "Endpoint For Creating Comments.")
    public ResponseEntity<Void> createComment(@RequestBody CommentDto commentDto) {

        commentService.save(commentDto);
        return new ResponseEntity<>(CREATED);
    }

    @Operation(summary = "Endpoint To Get All The Comments Of A Particular Post.")
    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK).body(commentService.getAllCommentsForPost(postId));
    }

    @GetMapping("/by-user/{userName}")
    @Operation(summary = "Endpoint To Get All The Comments Made By A Particular User.")
    public ResponseEntity<List<CommentDto>> getAllCommentsForUser(@PathVariable String userName) {
        return ResponseEntity.status(OK).body(commentService.getAllCommentsForUser(userName));
    }

    @GetMapping("/by-post/{postId}/excel/reporter")
    public void exportToExcel(@PathVariable Long postId,HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_category";
        response.setHeader(headerKey, headerValue);

        List<CommentDto> commentResponse = commentService.getAllCommentsForPost(postId);

        CommentExcelExporter excelExporter = new CommentExcelExporter(
                commentResponse);

        excelExporter.export(response);
    }
}
