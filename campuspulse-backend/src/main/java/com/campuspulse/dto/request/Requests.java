package com.campuspulse.dto.request;
import jakarta.validation.constraints.*;import com.campuspulse.enums.*;
public class Requests{
public record Signup(@NotBlank @Size(min=2,max=100) String name,@NotBlank @Email String email,@NotBlank @Size(min=8,max=100) String password){}
public record Login(@NotBlank String email,@NotBlank String password){}
public record Profile(@NotBlank @Size(min=2,max=100) String name){}
public record CreateComplaint(@NotBlank @Size(min=5,max=150) String title,@NotBlank @Size(min=15,max=5000) String description,@NotBlank @Size(min=3,max=150) String location){}
public record Analyze(@NotBlank String title,@NotBlank String description){}
public record Assign(@NotNull Long staffId){}
public record SetPriority(@NotNull Priority priority){}
public record SetStatus(@NotNull Status status,@NotBlank @Size(min=3,max=2000) String note){}
public record Reject(@NotBlank @Size(min=3,max=2000) String note){}
}
