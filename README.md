# Ransomware_Maturità
Progetto di maturità all'ITTS Vito Volterra (Ve)

The aim of this project is to emulate a ransomware, more specifically "wannacry" . When I started development on this project wannacry was a true threath and many people were still obvious on how it worked.

HOW THIS WORKS :

When a client connects to the Server, the server will generate using RSA (192bits prime numbers) a pair of key (public and private) and then send them back to the client closing the connection when the client sends an ack.

The client will then generate a simmetric key and ecrypt recursively from the given folder every file using the simmetric key. Once this is over, the simmetric key will be encoded in Base64 and then encrypted and stored in the hdd (the pair of private and public key will be stored in clear, the client shouldn't have them only the server but for safety reasons the client should have them).

Now for the good part, we have a software that can decrypt! (Unlike wannacry)

This software takes the private and public keys, uses them to decrypt the simmetric key, decodes it from Base64 and then decrypt every file in the requested folder.


This software eluded a lot of antiviruses (dunno why) so if you are gonna test it of modify I suggest a vm and I strongly advise against modifying the software to only encrypt using the simmetric key and deleting it after that, files will be corrupted without a chance to restore them.

There are prob a lot of typos, don't judge me
