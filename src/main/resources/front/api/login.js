function loginApi(data) {
    return $axios({
        'url': '/user/login',
        'method': 'post',
        data
    })
}

// function sendMsgApi(data) {
//     return $axios({
//         url: '/user/sendMsg',
//         method: 'post',
//         data: JSON.stringify(data),
//         headers: headers
//     })
// }

function loginoutApi() {
    return $axios({
        'url': '/user/loginout',
        'method': 'post',
    })
}

  