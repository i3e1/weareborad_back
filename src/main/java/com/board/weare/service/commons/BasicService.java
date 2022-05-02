package com.board.weare.service.commons;

import java.util.List;

public interface BasicService<Entity, REQUEST, Info> {
    public Entity get(Long id) ;
    public List<Entity> getAll();
    public Entity post(REQUEST request);
    public void patch(Info info);
    public void delete(Long id);
}
