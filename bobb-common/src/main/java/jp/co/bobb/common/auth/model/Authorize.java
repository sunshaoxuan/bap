package jp.co.bobb.common.auth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * describe:
 *
 * @author Parker Sun
 * @date 2018/10/22
 */
@Data
public class Authorize implements Serializable {
    private Collection<String> resources = new ArrayList<>();
    private Collection<String> roles = new ArrayList<>();
}
