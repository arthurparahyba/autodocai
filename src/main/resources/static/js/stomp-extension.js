function createStompMessageElement(message, model) {
    if(!model) {
        var p = document.createElement('p');
        p.textContent = message;
        return p;
    }

    var modelElement = document.querySelector(model);

    var element = document.createElement(modelElement.tagName);
    modelElement.classList.forEach(className => {
        element.classList.add(className);
    });
    element.textContent = message;
    return element;
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
            var model = elt.getAttribute('stomp-model');

            var socket = new SockJS(socketUrl);
            var stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                stompClient.subscribe(topic, function (message) {
                    console.log("Message: " + message.body);
                    var messageBody = message.body;
                    var element = createStompMessageElement(messageBody, model)
                    document.querySelector(target).appendChild(element);
                });
            });
        }
    });
});

