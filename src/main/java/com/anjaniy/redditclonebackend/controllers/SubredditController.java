package com.anjaniy.redditclonebackend.controllers;

import com.anjaniy.redditclonebackend.dto.SubredditDto;
import com.anjaniy.redditclonebackend.services.SubredditService;
import com.anjaniy.redditclonebackend.utilities.SubredditExcelExporter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/subreddit")
@AllArgsConstructor
@Tag(name = "Subreddit REST Endpoint")
public class SubredditController {

    @Autowired
    private SubredditService subredditService;

    @PostMapping
    @Operation(summary = "Endpoint For Creating Subreddits.")
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

    @GetMapping
    @Operation(summary = "Endpoint To Get All The Subreddits.")
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Endpoint To Get A Subreddit By Its Corresponding ID.")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubreddit(id));
    }

    @Transactional(readOnly = true)
    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=result_category";
        response.setHeader(headerKey, headerValue);

        List<SubredditDto> subredditResponse = subredditService.getAll();

        SubredditExcelExporter excelExporter = new SubredditExcelExporter(
                subredditResponse);

        excelExporter.export(response);
    }
}
