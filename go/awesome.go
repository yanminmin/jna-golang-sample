package main

/*
#include <stdlib.h>
*/
import "C"

import (
	"crypto/ecdsa"
	"crypto/elliptic"
	"crypto/rand"
	"crypto/sha512"
	"encoding/hex"
	"fmt"
	"math"
	s "sort"
	"sync"
)

var (
	key *ecdsa.PrivateKey
)

func init() {
	fmt.Println("init go")
	var err error
	key, err = ecdsa.GenerateKey(elliptic.P256(), rand.Reader)
	if err != nil {
		panic(err)
	}
}

var count int
var mtx sync.Mutex

//export add
func add(a, b int) int {
	return a + b
}

//export cosine
func cosine(x float64) float64 {
	return math.Cos(x)
}

//export sort
func sort(arrays []int) {
	s.Ints(arrays)
}

//export print
func print(msg string) int {
	mtx.Lock()
	defer mtx.Unlock()
	fmt.Println(msg)
	count++
	return count
}

//export ReturnByteSlice
func ReturnByteSlice(msg string) *C.char {
	//msg string
	fmt.Println("开21始")
	md := sha512.New()
	md.Write([]byte(msg)) // the private key,
	h := md.Sum(nil)[:32]
	//fmt.Println("h", hex.EncodeToString(h))
	// and compute ChopMD-256(SHA-512),
	r, s, err := ecdsa.Sign(rand.Reader, key, h)
	if err != nil {
		return C.CString("")
	}
	rBytes := r.Bytes()
	sBytes := s.Bytes()

	//fmt.Println("r", hex.EncodeToString(rBytes))
	//fmt.Println("s", hex.EncodeToString(sBytes))
	Nlen := bitsToBytes((key.PublicKey.Curve.Params().N).BitLen())
	signature := make([]byte, 2*Nlen)
	// pad the signature with zeroes
	copy(signature[Nlen-len(rBytes):Nlen], rBytes)
	copy(signature[2*Nlen-len(sBytes):], sBytes)
	return C.CString(hex.EncodeToString(signature))
}

func bitsToBytes(bits int) int {
	return (bits + 7) >> 3
}

//export echoString
func echoString(msg string) *C.char {
	return C.CString(msg)
}

//export echoWString
func echoWString(msg string) *C.char {
	return C.CString(msg)
}

//export echoGoString
func echoGoString(msg string) *C.char {
	return C.CString(msg)
}

func main() {}
