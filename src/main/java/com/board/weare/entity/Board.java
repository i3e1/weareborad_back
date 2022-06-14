package com.board.weare.entity;

import com.board.weare.dto.AccountDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_board")
@Getter
@ToString
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @Column(name = "content", length = 3000, nullable = false)
    private String content;

    @Column(name = "category", length = 30)
    private String category;

    @Column(name = "etc", length = 30)
    private String etc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", updatable = false, foreignKey = @ForeignKey(name = "tb_board_writer_id_fk"))
    private Account writer;

    @CreationTimestamp // INSERT 시 자동으로 값을 채워줌
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "crtd_dt", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp // UPDATE 시 자동으로 값을 채워줌
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "updt_dt")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Builder
    public Board(String title, String content, String category, String etc, Account writer) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.etc = etc;
        this.writer = writer;
    }

    public void update(String title, String content, String category, String etc) {
        if (title != null) this.title = title;
        if (content != null) this.content = content;
        if (category != null) this.category = category;
        if (etc != null) this.etc = etc;
    }
}
