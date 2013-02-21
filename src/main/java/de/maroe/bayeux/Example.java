package de.maroe.bayeux;

import java.text.MessageFormat;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;

public class Example {

	public void attach() {
		final BayeuxClient client = new BayeuxClient(
				"http://localhost:3000/faye", LongPollingTransport.create(null));
		client.handshake();	
		client.waitFor(1000, BayeuxClient.State.CONNECTED);
		ClientSessionChannel channel = client.getChannel("/nodes");
		ClientSessionChannel channel2 = client.getChannel("/nodes/private");
		channel.subscribe(new ClientSessionChannel.MessageListener() {
			public void onMessage(ClientSessionChannel channel, Message message) {
				System.out.println(MessageFormat.format(
						"Got {0} on Channel {1}", message.getData(), channel));
			}
		});
		
		channel2.subscribe(new ClientSessionChannel.MessageListener() {
			public void onMessage(ClientSessionChannel channel, Message message) {
				System.err.println(MessageFormat.format(
						"Got {0} on Channel {1}", message.getData(), channel));
			}
		});
		boolean t = true;
		
		while (t) {
			channel.publish("Hello Ruby");
		}
		
		client.disconnect();
		client.waitFor(1000, BayeuxClient.State.DISCONNECTED);
	}

	public static void main(String[] args) {
		Example example = new Example();
		example.attach();
	}
}