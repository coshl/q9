
package com.summer.util.risk;

/**
 * 解析时帮助
 *
 * @author xuzhen.qxz
 */
public abstract class AbstractLocalResponseHandler {

    protected String uriId;

    protected long startTime = System.currentTimeMillis();

    public String getUriId() {
        return uriId;
    }

    public void setUriId(String uriId) {
        this.uriId = uriId;
    }
}
