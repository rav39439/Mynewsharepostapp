//$("#newform").submit(function(e){
//	
//	e.preventDefault();
//	console.log(e.target.children[1])
//	 var name=e.target.children[1].value;
//	 var degree=e.target.children[3].value;
//	 var age=e.target.children[5].value;
//
//	 console.log(age)
//	 console.log(degree)
//	 console.log(name)
//	 
//	$.ajax({
//	 
//	 type:"POST",
//	url:"/saveuser",
//	 datatype:"json",
//	 data:{name:name,degree:degree,age:age},
//	 success:function(response){
//		 alert(response)
//	 
//	var finalresponse=JSON.stringify(response);
//	 $("#newresponse").html("<b>YOur response is:"+finalresponse+"</b>");
//	 
//	 
//	 
//	 
//	 }
//	 
//	
//})
//	 
//	 return false;
//	 })
//	 
//	 
//	import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.8.1/firebase-app.js'; 
//	
//	import { getStorage,deleteObject,ref } from 'https://www.gstatic.com/firebasejs/9.8.1/firebase-storage.js'; 

	//import {Stomp} from '/META-INF/resources/webjars/stomp-websocket/2.3.3/stomp.min.js' ;
	 
	 function senddata(form){
	
		 
	//e.preventDefault();
	
	//alert("comment")
	//console.log(e.target.children[3])
	 var id=form.id.value;
	var comments=form.comments.value;
//	 var age=e.target.children[5].value;

//	 console.log(age)
//	 console.log(degree)
	 console.log(comments);
	 console.log(id)
	 
	$.ajax({
	 
	 type:"POST",
	url:"/user/postcomment",
	 datatype:"json",
	 data:{comments:comments,id:id},
	 success:function(response){
		 //alert(response)
	 
	var finalresponse=JSON.stringify(response);
		 
		 
		 let htm="";
		 
		 
		 
		 response.forEach(function(elem){
			 console.log(elem.date)
		
			  htm+=`
			  <div class="bg-light border">
			  
			  <div class="d-flex flex-start align-items-center">
	              
	              <div>
	                <h6 class="fw-bold text-primary mb-1">${elem.name}</h6>
	                <p class="date">
	                ${elem.date}
	                
	                </p>
	                 
	              </div>
	            </div>

	 <p class=" mb-4 pb-2">${elem.comments}</p>
	  </div><br>
	 
	 `;
			
		 })
		 
		
		 
		 
		 
	 $("#mycardbody"+id).html(htm);
	 
	 
	 
	 
	 }
	 
	
})
	 
	 return false;
	 }
	 
	 
	 
	 function getdata(form){
			
		 
			//e.preventDefault();
			
			//alert("getcomments")
			//console.log(e.target.children[3])
			 var nid=form.nid.value;
			
			
			 console.log(nid)
			 
			$.ajax({
			 
			 type:"POST",
			url:"/user/getcomment",
			 datatype:"json",
			 data:{nid:nid},
			 success:function(response){
				// alert(response)
			 
			var finalresponse=JSON.stringify(response);
				 
				 
				 let htm="";
				 
				 
				 
				 response.forEach(function(elem){
					 console.log(elem.date)
				
					  htm+=`
					  <div class="bg-light border">
					  
					  <div class="d-flex flex-start align-items-center">
			              
			              <div>
			                <h6 class="fw-bold text-primary mb-1">${elem.name}</h6>
			                <p class="date">
			                ${elem.date}
			                
			                </p>
			                 
			              </div>
			            </div>

			 <p class=" mb-4 pb-2">${elem.comments}</p>
			  </div><br>
			 
			 `;
					
				 })
				 
				
				 
				 
				 
			 $("#mycardbody"+nid).html(htm);
			 
			 
			 
			 
			 }
			 
			
		})
			 
			 return false;
			 }
			  
	 
	 
	 
//---------------------------uploading files to firebase-----------------------------------------------------------
	 
	 let myurl=""


		    const firebaseConfig = {
		    apiKey: "AIzaSyAZ9x_QbhCfY9aEsqxEiYhWtfIRNGl_qeo",
		    authDomain: "mynewproject-ae49a.firebaseapp.com",
		    projectId: "mynewproject-ae49a",
		    storageBucket: "mynewproject-ae49a.appspot.com",
		    messagingSenderId: "939005557600",
		    appId: "1:939005557600:web:ee766070db4bd2dadfbc05",
		    measurementId: "G-YFKTT151SN"
		  };

		  firebase.initializeApp(firebaseConfig);
		    console.log(firebase);
	 
		    async function uploadImage() {
		        const ref = firebase.storage().ref();
		        const file = document.querySelector("#photo").files[0];
		        const name = +new Date() + "-" + file.name;
		        const metadata = {
		          contentType: file.type
		        };
		       // function mydata(callback){
		           try{
		        await ref.child(name).put(file, metadata).then(snapshot => snapshot.ref.getDownloadURL())
		          .then(url => {
		              myurl=url

		              callback(url)
		              //localStorage.setItem("url",url)
		            //console.log(url);
		           
		          })
		      }
		      catch(err){
		          console.log(err)
		      }

		     // }

		     function callback(url){
		         console.log(url)
		         newcallback(url)
		       //newurl=url
		     }
		      }
		      
		  function newcallback(url){
		      console.log("the iamge is "+url)
		      
		      document.getElementById("myfile").value = url;
		      document.getElementById("pic").innerHTML =`<b>uploaded</b>`;

		      
		  }
		      
//------------------------------------------------------------------------------------------------------------------	 
	 
//--------------------------------delete data---------------------------------------------------------------------
		  
		 

		  
		  
		  
		  
		  
		  
//		   function deleteImage(name) {
//			  alert("deleting")
//			  //firebase.initializeApp(firebaseConfig);
//			 // console.log(firebase.storage().ref().child(name))
//			  
//			  let storage=firebase.storage();
//			  
//			  let storageRef = storage.reference(name)
//
//
//			 //string aname= name.split(RegExp(r'(%2F)..*(%2F)'))[1].split(".")[0];
//		        const desertRef = firebase.storage().ref(storage,storageRef.name);
//		        deleteObject(desertRef).then(()=>{
//		        	console.log("deleted")
//		        	
//		        }).catch((err)=>{
//		          console.log(err)
//		      })	  
//		      return false;
//		  }	  
		      
		 		  
		  
//---------------------------------------------------------------------------------------------------------------
		  
//--------------------------------------update image------------------------------------------------------------
		      
		      async function updateImage() {
			        const ref = firebase.storage().ref();
			        let image=document.getElementById("myfile").value;
			        //deleteImage(image);
			        
			        const file = document.querySelector("#photo").files[0];
			        const name = +new Date() + "-" + file.name;
			        const metadata = {
			          contentType: file.type
			        };
			       // function mydata(callback){
			           try{
			        await ref.child(name).put(file, metadata).then(snapshot => snapshot.ref.getDownloadURL())
			          .then(url => {
			              newurl=url

			              callback1(url)
			              //localStorage.setItem("url",url)
			            //console.log(url);
			           
			          })
			      }
			      catch(err){
			          console.log(err)
			      }

			     // }

			     function callback1(url){
			         console.log(url)
			         newcallback1(url)
			       //newurl=url
			     }
			      }
			      
			  function newcallback1(url){
			      console.log("the iamge is "+url)
			      
			      document.getElementById("myfile").value = url;
			      document.getElementById("pic").innerHTML =`<b>uploaded</b>`;

			  }      
		      
		      
		      
		      
		      
//--------------------------------------------------------------------------------------------------------------		      
		  
		  
		  
		//$("#newform").submit(function(e){
		//	
//			e.preventDefault();
//			console.log(e.target.children[1])
//			 var name=e.target.children[1].value;
//			 var degree=e.target.children[3].value;
//			 var age=e.target.children[5].value;
		//
//			 console.log(age)
//			 console.log(degree)
//			 console.log(name)
//			 
//			$.ajax({
//			 
//			 type:"POST",
//			url:"/saveuser",
//			 datatype:"json",
//			 data:{name:name,degree:degree,age:age},
//			 success:function(response){
//				 alert(response)
//			 
//			var finalresponse=JSON.stringify(response);
//			 $("#newresponse").html("<b>YOur response is:"+finalresponse+"</b>");
//			 
//			 
//			 
//			 
//			 }
//			 
		//	
		//})
//			 
//			 return false;
//			 })
//			 
			 
			 
			 
	//----------------------------------------------how to send value via function and jquery--------------------		 
		 function myform(form){
			
				 
		///e.preventDefault();
//		 var name=e.target.children[1].value;
//		 var degree=e.target.children[3].value;
//		 var myfile=e.target.children[5].value;
			
			//console.log(e.target.children[3])
			 var degree=form.degree.value;
			var name=form.name.value;			 
			
			var myfile=form.myfile.value;
			//alert(myfile);
			//alert(degree);
			 console.log(name)
			 console.log(degree)
			 console.log(myfile)
			alert("comment") 
			$.ajax({
			 
			 type:"POST",
			url:"/saveuser",
			 datatype:"json",
			 data:{name:name,degree:degree,myfile:myfile},
			 success:function(response){
				
			 
			var finalresponse=JSON.stringify(response);
			// alert(finalresponse)
			document.getElementById("newresponse").innerHTML="<b>"+finalresponse+"</b>";	
				
				
				
				 
			 }	 
				 
			 
			
		})
			 
			 return false;
			 }
	//------------------------------------------------------------------------------------------------------------ 
	 
		function deletedata(form){
			alert("dsafasf")
			var cid=form.cId.value;
			var imgname=form.image.value
			//deleteImage(imgname);
			console.log(imgname)
			console.log(cid)
			
			
			
				
				$.ajax({
					 
					 type:"POST",
					url:"/user/deletedata",
					 datatype:"json",
					 data:{cid:cid},
					 success:function(response){
						
					 
					var finalresponse=JSON.stringify(response);
					// alert(finalresponse)
						
						
						
						 
					 }	 
				
				})
			
		return false;
		}
		 
		
		
		
		 
		function getdatan(form){
			
			 
			//e.preventDefault();
			
			//alert("getcomments")
			//console.log(e.target.children[3])
			 var nid=form.nid.value;
			
			
			 console.log(nid)
			 
			$.ajax({
			 
			 type:"POST",
			url:"/getcomments",
			 datatype:"json",
			 data:{nid:nid},
			 success:function(response){
				 //alert(response)
			 
			var finalresponse=JSON.stringify(response);
				 
				 
				 let htm="";
				 
				 
				 
				 response.forEach(function(elem){
					 console.log(elem.date)
				
					  htm+=`
					  <div class="bg-light border">
					  
					  <div class="d-flex flex-start align-items-center">
			              
			              <div>
			                <h6 class="fw-bold text-primary mb-1">${elem.name}</h6>
			                <p class="date">
			                ${elem.date}
			                
			                </p>
			                 
			              </div>
			            </div>

			 <p class=" mb-4 pb-2">${elem.comments}</p>
			  </div><br>
			 
			 `;
					
				 })
				 
				
				 
				 
				 
			 $("#mycardbody"+nid).html(htm);
			 
			 
			 
			 
			 }
			 
			
		})
			 
			 return false;
			 }
			  
		 
		
		
		
		
	//-----------------------------------------------chat---------------------------------------------------------------	
		
		var stompclient=null

		function sendMessage(){
			
			
			let jsonOb={
					
					name:$("#name").val(),
					content:$("#message-value").val()
			}
			stompclient.send("/app/Message",{},JSON.stringify(jsonob))
			
		}







		function connect(){
			let fname=document.getElementById('tname').value
			
			
			console.log(Stomp)
			
			
		var socket = new SockJS("http://localhost:9000/stomp");
			stompclient=Stomp.over(socket);
			stompclient.connect({},function(frame){
				console.log("Connected :"+frame)
				
				stompclient.subscribe("/topic/return-to",function(response){
					showmessage(JSON.parse(response.body))
				})
			})
			
		}


		function showmessage(message){
			
			$("#message-container-table").prepend(`<tr><td><b>${message.name} :</b><b>${message.content}</b></td></tr>`)
			
		}


		
		
		

		$(document).ready((e)=>{
			
			
			connect();
			
			console.log("jdghfag")

			
			
			
			$("#send-btn").click(function(){
				
				sendMessage();
				
			})
				
				
				$("#logout").click(()=>{
					if(stompclient!==null){
						stomclient.disconnect();
						window.location.href=("/getfriends")
					}
				})
			
		})



		
		
		
		
		
		
	 