package binarylei.shardingjdbc.mapper;

import binarylei.shardingjdbc.entity.OrderItem;

public interface OrderItemMapper {
    int deleteByPrimaryKey(Integer itemId);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer itemId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}