package apps.kata.spideymessenger.modelos

class MensajesChat (val id: String, val texto: String, val deId: String, val paraId: String, val tiempo: Long) {
    constructor() : this("", "", "", "", -1)
}