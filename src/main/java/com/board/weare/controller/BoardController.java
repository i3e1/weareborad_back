package com.board.weare.controller;

import com.board.weare.dto.BoardDto;
import com.board.weare.entity.Board;
import com.board.weare.service.BoardServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private BoardServiceImpl boardService;

    @ApiOperation(value = "게시글 단일 조회", notes = "게시글 정보 한 개 조회.")
    @PostMapping("/boards/{boardId}")
    public ResponseEntity<BoardDto.Info> postBoard(@Valid @PathVariable(name = "boardId") Long boardId){
        Board board = boardService.getById(boardId);
        BoardDto.Info info = new BoardDto.Info(board);
        return new ResponseEntity<>(info, HttpStatus.CREATED);
    }

    @ApiOperation(value = "게시글 전체 조회", notes = "게시글 정보 한 개 조회.")
    @PostMapping("/boards/{boardId}")
    public ResponseEntity<List<BoardDto.Info>> postBoard(@Valid @RequestParam(name = "category", defaultValue = "notice") String category){
        List<BoardDto.Info> boards = boardService.getAllByCategory(category);
        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    @ApiOperation(value = "게시글 등록", notes = "게시글 정보를 등록한다.")
    @PostMapping("/board")
    public ResponseEntity<BoardDto.Info> postBoard(@Valid @RequestBody BoardDto.Request req){
        Board board = boardService.post(req);
        BoardDto.Info info = new BoardDto.Info(board);
        return new ResponseEntity<>(info, HttpStatus.CREATED);
    }

}
