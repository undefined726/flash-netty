package the.flash.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import the.flash.protocol.request.CreateGroupRequestPacket;
import the.flash.protocol.response.CreateGroupResponsePacket;
import the.flash.util.IDUtil;
import the.flash.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) {
        List<String> userIdList = createGroupRequestPacket.getUserIdList();

        List<String> userNameList = new ArrayList<>();
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        for (String userId : userIdList) {
            Channel channel = SessionUtil.getChannel(userId);
            if (channel != null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        }

        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupId(IDUtil.randomId());
        createGroupResponsePacket.setUserNameList(userNameList);

        // 给每个客户端发送拉群通知
        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.print("群创建成功，id 为[" + createGroupResponsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());

    }
}