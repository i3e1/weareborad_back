package com.board.weare.service.interfaces;


import com.board.weare.dto.AccountDto;
import com.board.weare.dto.BoardDto;
import com.board.weare.entity.Account;
import com.board.weare.entity.Board;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    Board getById(Long id);
    List<BoardDto.Info> getAllByCategory(String category);
    Optional<Board> findById(Long id);



    Board post(BoardDto.Request req);

    Board patch(BoardDto.Request req);

    boolean delete(Long id);
}
