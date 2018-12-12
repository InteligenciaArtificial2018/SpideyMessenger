package apps.kata.spideymessenger.modelos

class MensajesChat (val id: String, val text: String, val recibirId: String, val enviarId: String, val tiempo: Long) {
    constructor() : this("", "", "", "", -1)
}