package com.bdqn.ItemService;

import com.bdqn.Dao.repotory;
import com.bdqn.pojo.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ServiceImpl  implements  service{

    @Autowired
   private  repotory repotory;

    @Override
    public List<Item> findAll(Item item) {
        Example<Item> example = Example.of(item);
        List<Item> repotoryAll = repotory.findAll(example);

        return repotoryAll;
    }

    @Override
    @Transactional
    public void save(Item item) {
        this.repotory.save(item);

    }
}
