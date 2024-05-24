package com.aireview.review.domain.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode
public class Email {
    private static final Pattern pattern = Pattern.compile("[\\w~\\-.+]+@[\\w~\\-]+(\\.[\\w~\\-]+)+");
    private final String address;

    public Email(String address) {
        Assert.hasLength(address, "address must be provided");
        Assert.isTrue(address.length() >= 4 && address.length() <= 50,
                "address length must be between 4 and 50 characters");
        Assert.isTrue(checkAddress(address)
                , String.format("invalid address : %s", address));
        this.address = address;
    }

    private static boolean checkAddress(String address) {
        return pattern.matcher(address).matches();
    }

    public static Email of(String address) {
        return new Email(address);
    }
}


