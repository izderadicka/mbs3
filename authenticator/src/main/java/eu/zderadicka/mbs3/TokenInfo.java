package eu.zderadicka.mbs3;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TokenInfo {
    public static String issuer = "https://zderadicka.eu";
    @NotBlank
    public String upn; 
    @NotNull
    @Size(min=1)
    public Set<String> groups;
    public Long validSeconds;

    
}
