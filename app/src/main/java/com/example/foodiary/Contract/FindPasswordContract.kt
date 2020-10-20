package com.example.foodiary.Contract

class FindPasswordContract {
    interface View{
        fun setPresenter(presenter: Presenter)
        fun replacePage(email: String)
        fun setLottie()

    }
    interface Presenter{
        fun attachView(view: View)
        fun detachView()
        fun sendEmail(email : String)
    }
}