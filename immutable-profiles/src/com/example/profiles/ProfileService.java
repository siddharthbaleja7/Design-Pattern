package com.example.profiles;

/**
 * Assembles profiles with scattered, inconsistent validation.
 */
public class ProfileService {

    // returns a fully built profile but mutates it afterwards (bug-friendly)
    public UserProfile createMinimal(String id, String email) {

        return new UserProfile.Builder(id, email).build();
    }

    public UserProfile updateDisplayName(UserProfile p, String displayName) {
        // Objects.requireNonNull(p, "profile");
        // if (displayName != null && displayName.length() > 100) {
        //     // silently trim (inconsistent policy)
        //     displayName = displayName.substring(0, 100);
        // }
        // p.setDisplayName(displayName); // mutability leak
        return new UserProfile.Builder(p.getId(), p.getEmail())
                .phone(p.getPhone())
                .displayName(displayName)
                .address(p.getAddress())
                .marketingOptIn(p.isMarketingOptIn())
                .twitter(p.getTwitter())
                .github(p.getGithub())
                .build();
    }
}
