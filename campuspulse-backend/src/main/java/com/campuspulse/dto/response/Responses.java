package com.campuspulse.dto.response;
import com.campuspulse.entity.*;import com.campuspulse.enums.*;import java.time.Instant;
public class Responses{
public record UserView(Long id,String name,String email,Role role){public static UserView of(User u){return new UserView(u.id,u.name,u.email,u.role);}}
public record AuthView(String token,String tokenType,UserView user){}
public record Analysis(Category category,Priority priority,String explanation){}
public record ComplaintView(Long id,String title,String description,String location,Category category,Priority priority,Status status,String aiExplanation,String studentName,String assignedStaffName,Instant createdAt,Instant updatedAt){}
public record HistoryView(Long id,Status status,String note,String actorName,Instant createdAt){public static HistoryView of(ComplaintHistory h){return new HistoryView(h.id,h.status,h.note,h.actor.name,h.createdAt);}}
}
