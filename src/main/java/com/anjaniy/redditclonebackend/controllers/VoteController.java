package com.anjaniy.redditclonebackend.controllers;

import com.anjaniy.redditclonebackend.dto.PostResponse;
import com.anjaniy.redditclonebackend.dto.SubredditDto;
import com.anjaniy.redditclonebackend.dto.VoteDto;
import com.anjaniy.redditclonebackend.services.PostService;
import com.anjaniy.redditclonebackend.services.VoteService;
import com.anjaniy.redditclonebackend.utilities.SubredditExcelExporter;
import com.anjaniy.redditclonebackend.utilities.VoteExcelExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})

@RestController
@RequestMapping("/votes")
@AllArgsConstructor
@Tag(name = "Vote REST Endpoint")
public class VoteController {

    @Autowired
    private VoteService voteService;
    @Autowired
    private PostService postService;

    @PostMapping
    @Operation(summary = "Endpoint For Voting (UPVOTE / DOWNVOTE).")
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_category";
        response.setHeader(headerKey, headerValue);

        List<PostResponse> postResponses = postService.getAllPosts();

        VoteExcelExporter excelExporter = new VoteExcelExporter(
                postResponses);

        excelExporter.export(response);
    }
}