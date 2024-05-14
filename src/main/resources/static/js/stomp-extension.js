function createLoading() {
    var loadingElement = document.createElement('div');
    loadingElement.className = 'inline-block animate-spin rounded-full h-5 w-5 mt-2 border-t-2 border-b-2 border-purple-500';
    return loadingElement;
}


function createTagContent(tagId, style) {
    var div = document.createElement('div');
    div.className = style;

    var divContent = document.createElement('div');
    divContent.id = tagId;
    div.appendChild(divContent);

    var loading = createLoading();
    loading.id = "loading-"+tagId;
    div.appendChild(loading);

    return div;
}

document.addEventListener('DOMContentLoaded', (event) => {
    htmx.defineExtension('stomp', {
        onEvent: function (name, evt) {
            if (name === "htmx:beforeProcessNode") {
                this.initStomp(evt.target);
            }
        },
        initStomp: function (elt) {
            var socketUrl = elt.getAttribute('stomp-url');
            var topic = elt.getAttribute('stomp-topic');
            var target = elt.getAttribute('stomp-target');
            var style = elt.getAttribute('stomp-style-tag');

            var socket = new SockJS(socketUrl);
            var stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                stompClient.subscribe(topic, function (message) {

                    if(!document.getElementById(message.headers.tagId)){
                        var tag = createTagContent(message.headers.tagId, style);
                        document.querySelector(target).appendChild(tag);
                    }

                    else if(message.headers.type == 'GRAPHIC') {
                        htmx.trigger(target, 'graficoRecebido', { tagId: message.headers.tagId, content: message.body, type:message.headers.type});
                        return;
                    } else if(message.headers.type == 'DOCUMENT'){
                         htmx.trigger(target, 'documentoRecebido', { tagId: message.headers.tagId, content: message.body, type:message.headers.type});
                         return;
                     }

                    console.log("Mensagem recebida sem um tipo especificado:");
                    console.log(message);
                });
            });
        }
    });
});

