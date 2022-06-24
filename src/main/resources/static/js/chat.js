function connect() {
	
	let user1=$("#myuserid").val()
	
	let user2=+$("#userid").val()
	
	let userid=parseInt(user1)+parseInt(user2);
	
	
	//let userid1=user2+user1
	console.log(userid)
	
		//console.log(userid1)

	var socket = new SockJS('/websocket');
	stompClient = Stomp.over(socket);
	
	stompClient.connect({}, function (frame) {
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/chat/'+userid,
				
				function (chatMessage) {
					showChatMessage(JSON.parse(chatMessage.body));			
				});
		stompClient.send(`/app/send/${userid}`, {}, JSON.stringify({'messageType': 'JOIN','to':user1,'from':user2,'message':null}));
		
	});
	
	
	
	

}

function showChatMessage(chatMessage){
	console.log("message: "+chatMessage);
	//$("#chat").append("<p>" + chatMessage.message + "</p>");
	
	if(chatMessage.messageType === 'JOIN') {
		$("#chat").append('<p>'+chatMessage.name+' has joined</p>');
	}else if(chatMessage.messageType === 'LEAVE'){
		$("#chat").append('<p>'+chatMessage.name+' has left</p>');
	}else{
	$("#chat").append('<div class="card mb-2"><div class="card-body"><h5 class="card-title">'+chatMessage.name+'</h5><p class="card-text">'+ chatMessage.message +'</p></div><small>'+chatMessage.date+'</small></div>');
	}
}

function sendMessage(){
	
let user1=$("#myuserid").val()
	
	let user2=+$("#userid").val()
	
	let userid=parseInt(user1)+parseInt(user2);
	console.log(userid)
	
	//console.log(userid1)
	stompClient.send("/app/send/"+userid, {}, JSON.stringify({'message': $("#message").val(), 'messageType': 'CHAT','to':user1,'from':user2}));
	$("#message").val('');
	
	
}

$(document).ready(function () {
	connect();
	$("#chat-form").submit(function(e){
        e.preventDefault();
    });


    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
});

///--------------------------sending messages via routes--------------------------------------------------------------
//var stompClient = null;
//var notificationCount = 0;
//
//$(document).ready(function() {
//    console.log("Index page is ready");
//    connect();
//
//    $("#send").click(function() {
//        sendMessage();
//    });
//
//    $("#send-private").click(function() {
//        sendPrivateMessage();
//    });
//
////    $("#notifications").click(function() {
////        resetNotificationCount();
////    });
//});
//
//function connect() {
//    var socket = new SockJS('/our-websocket');
//    stompClient = Stomp.over(socket);
//    stompClient.connect({}, function (frame) {
//        console.log('Connected: ' + frame);
//       // updateNotificationDisplay();
//        stompClient.subscribe('/topic/messages', function (message) {
//            showMessage(JSON.parse(message.body).content);
//        });
//
//        stompClient.subscribe('/user/topic/private-messages', function (message) {
//            showMessage(JSON.parse(message.body).content);
//        });
//
////        stompClient.subscribe('/topic/global-notifications', function (message) {
////            notificationCount = notificationCount + 1;
////            updateNotificationDisplay();
////        });
//
////        stompClient.subscribe('/user/topic/private-notifications', function (message) {
////            notificationCount = notificationCount + 1;
////            updateNotificationDisplay();
////        });
//    });
//}
//
//function showMessage(message) {
//    $("#messages").append("<tr><td>" + message + "</td></tr>");
//}
//
//function sendMessage() {
//    console.log("sending message");
//    stompClient.send("/ws/message", {}, JSON.stringify({'messageContent': $("#message").val()}));
//}
//
//function sendPrivateMessage() {
//    console.log("sending private message");
//    stompClient.send("/ws/private-message", {}, JSON.stringify({'messageContent': $("#private-message").val()}));
//}
//
////function updateNotificationDisplay() {
////    if (notificationCount == 0) {
////        $('#notifications').hide();
////    } else {
////        $('#notifications').show();
////        $('#notifications').text(notificationCount);
////    }
////}
////
////function resetNotificationCount() {
////    notificationCount = 0;
////    updateNotificationDisplay();
////}

///------------------------------------------------------------------------------------------------------------------




