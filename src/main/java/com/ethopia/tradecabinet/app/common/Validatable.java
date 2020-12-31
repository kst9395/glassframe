package com.ethopia.tradecabinet.app.common;

import java.util.List;

public interface Validatable {
    List<ErrorMessage> validate();

}
