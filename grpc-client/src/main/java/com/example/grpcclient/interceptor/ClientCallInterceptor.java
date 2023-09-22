package com.example.grpcclient.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.interceptor.GrpcGlobalClientInterceptor;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

@Slf4j
@GrpcGlobalClientInterceptor
public class ClientCallInterceptor implements ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions,
                                                               Channel channel) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(channel.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                log.info("Added metadata");
                headers.put(Metadata.Key.of("CLIENT-HEADER", ASCII_STRING_MARSHALLER), "HELLO_I'M_HEADER");
                super.start(responseListener, headers);
            }
        };
    }
}
