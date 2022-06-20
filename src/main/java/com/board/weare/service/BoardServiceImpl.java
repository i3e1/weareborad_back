package com.board.weare.service;

import com.board.weare.dto.AccountDto;
import com.board.weare.dto.BoardDto;
import com.board.weare.entity.Account;
import com.board.weare.entity.Board;
import com.board.weare.exception.BadRequestException;
import com.board.weare.exception.ForbiddenException;
import com.board.weare.exception.MyEntityNotFoundException;
import com.board.weare.exception.UnauthorizedException;
import com.board.weare.repository.AccountRepository;
import com.board.weare.repository.BoardRepository;
import com.board.weare.service.interfaces.AccountService;
import com.board.weare.service.interfaces.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
    //    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BoardRepository boardRepository;


    @Override
    public Board getById(Long id) {
        Optional<Board> boardOpt = findById(id);
        if(boardOpt.isPresent())return boardOpt.get();
        throw new MyEntityNotFoundException("board", "id로 조회 실패");
    }

    @Override
    public List<BoardDto.Info> getAllByCategory(String category) {
        return boardRepository.findByCategory(category);
    }

    @Override
    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    @Override
    public Board post(BoardDto.Request req) {
        Board board = new Board();
        String title = req.getTitle();
        String content = req.getContent();
        String category = req.getCategory();
        String etc = req.getEtc();
        board = patch(board, title, content, category, etc);
        boardRepository.save(board);
        return null;
    }


    public Board patch(Board board, String title, String content, String category, String etc) {
        board.update(title, content, category, etc);
        return board;
    }

    @Override
    public Board patch(BoardDto.Request req) {
        return null;
    }

    public boolean delete(Long id) {
        Account myInfo = AccountServiceImpl.getAccountFromSecurityContext();

        if (myInfo.isRoot() || myInfo.getUsername().equals(id)) {
            Optional<Board> boardOpt = findById(id);
            if (boardOpt.isPresent()) {
                boardRepository.delete(boardOpt.get());
                return true;
            }
            return false;
        }
        throw new ForbiddenException("게시글 조작 권한이 없습니다.");
    }

}
