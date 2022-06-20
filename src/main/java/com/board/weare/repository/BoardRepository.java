package com.board.weare.repository;


import com.board.weare.dto.BoardDto;
import com.board.weare.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, String> {

    List<BoardDto.Info> findByCategory(String category);
}
