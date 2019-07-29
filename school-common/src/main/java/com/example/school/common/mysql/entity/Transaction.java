package com.example.school.common.mysql.entity;

import com.example.school.common.base.entity.IdPageEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/06/19 16:10
 * description:
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "t_transaction")
public class Transaction extends IdPageEntity {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 标题
     */
    private String title;
    /**
     * 价格
     */
    private Double price;
    /**
     * 商品描述
     */
    private String describeContent;

    /**
     * 联系方式
     */
    private String contactMode;

    /**
     * 联系人
     */
    private String contactPeople;

    /**
     * 地址
     */
    private String address;
    /**
     * 浏览量
     */
    private Long browsingVolume;
    /**
     * 交易状态{inRelease：发布中，newRelease：新发布，afterRelease，发布后；solve:已解决,lowerShelf，下架}
     */
    private String state;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 删除状态：1已删除 0未删除
     */
    private Short deleteState;

    public Transaction(Long id, String title, Double price, String describeContent, String contactMode, String contactPeople, String address) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.describeContent = describeContent;
        this.contactMode = contactMode;
        this.contactPeople = contactPeople;
        this.address = address;
    }

    public Transaction(String sortName, String sortOrder, int pageNumber, int pageSize, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        super(sortName, sortOrder, pageNumber, pageSize, startDateTime, endDateTime);
    }

}
