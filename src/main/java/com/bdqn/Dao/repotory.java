package com.bdqn.Dao;

import com.bdqn.pojo.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface repotory  extends JpaRepository<Item,Long> {
    public List<Item> findAll();


}
