/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.transport.http.netty.internal.websocket;


import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.CloseReason;
import javax.websocket.RemoteEndpoint;


/**
 * This is spec implementation of {@link javax.websocket.Session} which uses {@link WebSocketSessionAdapter}.
 */
public class WebSocketSessionImpl extends WebSocketSessionAdapter {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionImpl.class);

    private final ChannelHandlerContext ctx;
    private final boolean isSecure;
    private final URI requestedUri;
    private final String sessionId;
    private boolean isOpen;

    public WebSocketSessionImpl(ChannelHandlerContext ctx, boolean isSecure,
                                String requestedUri, String sessionId) throws URISyntaxException {
        this.ctx = ctx;
        this.isSecure = isSecure;
        this.requestedUri = new URI(requestedUri);
        this.sessionId = sessionId;
        this.isOpen = true;
    }

    @Override
    public RemoteEndpoint.Basic getBasicRemote() {
        RemoteEndpoint.Basic basicRemoteEndpoint = new WebSocketBasicRemoteEndpoint(ctx);
        return basicRemoteEndpoint;
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public void close() throws IOException {
        ctx.channel().close();
    }

    @Override
    public void close(CloseReason closeReason) {
        ctx.channel().writeAndFlush(new CloseWebSocketFrame(closeReason.getCloseCode().getCode(),
                                                    closeReason.getReasonPhrase()));
    }

    @Override
    public URI getRequestURI() {
        return requestedUri;
    }

    @Override
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
