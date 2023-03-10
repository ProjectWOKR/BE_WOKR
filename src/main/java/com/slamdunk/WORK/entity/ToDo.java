package com.slamdunk.WORK.entity;

import com.slamdunk.WORK.dto.request.ToDoRequest;
import com.slamdunk.WORK.utill.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ToDo extends TimeStamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "toDo_id")
    private Long id;

    @Column(unique = true)
    private String toDo;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    private LocalDateTime registerDate;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private boolean display;

    @Column(nullable = false)
    private boolean completion;




    public ToDo(ToDoRequest param){
        this.toDo = param.getToDo();
        this.memo = param.getMemo();
        this.registerDate = param.getRegisterDate();
        this.startDate = param.getStartDate();
        this.endDate = param.getEndDate();
        this.priority = param.getPriority();
        this.display = param.isDisplay();
        this.completion = false;

    }



}