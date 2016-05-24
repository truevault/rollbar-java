package com.truevault.rollbar.payload.data;

import com.truevault.rollbar.utilities.Extensible;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents the HTTP request that triggered the error
 */
public class Request extends Extensible<Request> {
    public static final String URL_KEY = "url";
    public static final String METHOD_KEY = "method";
    public static final String HEADERS_KEY = "headers";
    public static final String PARAMS_KEY = "params";
    public static final String GET_KEY = "GET";
    public static final String QUERY_STRING_KEY = "query_string";
    public static final String POST_KEY = "POST";
    public static final String BODY_KEY = "body";
    public static final String USER_IP_KEY = "user_ip";
    private static final Set<String> keys = new HashSet<>();

    static {
        final String[] keys = new String[]{
                URL_KEY,
                METHOD_KEY,
                HEADERS_KEY,
                PARAMS_KEY,
                GET_KEY,
                QUERY_STRING_KEY,
                POST_KEY,
                BODY_KEY,
                USER_IP_KEY
        };
        Collections.addAll(Request.keys, keys);
    }

    @Override
    protected Set<String> getKnownMembers() {
        return keys;
    }

    @Override
    public Request copy() {
        return new Request(getMembers());
    }

    /**
     * @param members map of custom arguments
     */
    private Request(Map<String, Object> members) {
        super(members);
    }

    /**
     * @param url         the url
     * @param method      the HTTP method
     * @param headers     the HTTP Headers
     * @param params      the routing parameters (typically derived from your routing module)
     * @param get         the parsed query string parameters
     * @param queryString the raw query string
     * @param post        POST parameters
     * @param body        the raw POST body
     * @param userIp      the ip address of the affected user
     */
    public Request(String url, String method, Map<String, String> headers, Map<String, String> params,
            Map<String, String> get, String queryString, Map<String, Object> post, String body,
            InetAddress userIp) {
        this(url, method, headers, params, get, queryString, post, body, userIp, null);
    }

    /**
     * @param url         the url
     * @param method      the HTTP method
     * @param headers     the HTTP Headers
     * @param params      the routing parameters (typically derived from your routing module)
     * @param get         the parsed query string parameters
     * @param queryString the raw query string
     * @param post        POST parameters
     * @param body        the raw POST body
     * @param userIp      the ip address of the affected user
     * @param members     the custom arguments
     */
    public Request(String url, String method, Map<String, String> headers, Map<String, String> params,
            Map<String, String> get, String queryString, Map<String, Object> post, String body,
            InetAddress userIp, Map<String, Object> members) {
        super(members);
        putKnown(URL_KEY, url);
        putKnown(METHOD_KEY, method);
        putKnown(HEADERS_KEY, headers == null ? null : new LinkedHashMap<>(headers));
        putKnown(PARAMS_KEY, params == null ? null : new LinkedHashMap<>(params));
        putKnown(GET_KEY, get == null ? null : new LinkedHashMap<>(get));
        putKnown(QUERY_STRING_KEY, queryString);
        putKnown(POST_KEY, post == null ? null : new LinkedHashMap<>(post));
        putKnown(BODY_KEY, body);
        putKnown(USER_IP_KEY, userIp == null ? null : userIp.getHostAddress());
    }

    /**
     * @return the url
     */
    public String url() {
        return (String) get(URL_KEY);
    }

    /**
     * @return the HTTP method
     */
    public String method() {
        return (String) get(METHOD_KEY);
    }

    /**
     * @return the HTTP headers
     */
    public Map<String, String> headers() {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> headers = (LinkedHashMap<String, String>) get(HEADERS_KEY);
        return headers;
    }

    /**
     * @return the routing parameters, typically parsed out of the URL by your routing module
     */
    public Map<String, String> params() {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> params = (LinkedHashMap<String, String>) get(PARAMS_KEY);
        return params;
    }

    /**
     * Get the parsed query string parameters
     *
     * @return the parsed query string parameters
     */
    public Map<String, String> getGet() {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, String> get = (LinkedHashMap<String, String>) get(GET_KEY);
        return get;
    }

    /**
     * @return the raw query string
     */
    public String queryString() {
        return (String) get(QUERY_STRING_KEY);
    }

    /**
     * @return the parsed POST parameters
     */
    public Map<String, Object> post() {
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, Object> post = (LinkedHashMap<String, Object>) get(POST_KEY);
        return post;
    }

    /**
     * @return the raw POST body
     */
    public String body() {
        return (String) get(BODY_KEY);
    }

    /**
     * @return the affected user's IP address
     */
    public InetAddress userIp() {
        Object ip = get(USER_IP_KEY);
        try {
            return ip == null ? null : InetAddress.getByName((String) get(USER_IP_KEY));
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
