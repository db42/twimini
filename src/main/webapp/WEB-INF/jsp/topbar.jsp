<div class="topbar-permanant">
    <div class="container">
        <ul class = "left-pull">
            <li class="topbar-button home-button">
                <a href="/twimini/home"> Home </a>
            </li>
        </ul>
        <div class="icon-position">
            <i class="twitter-icon-embossed"></i>
        </div>
        <ul class = "right-pull">
            <li id="nav">
                <ul >
                    <li class="menu-button down-pic">
                        <ul>
                            <li id="settings-block">
                                <a href="/twimini/settings">Settings</a>
                            </li>
                            <li id="logout-block">
                                <a href="#" onclick="return logout()">Logout</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
            <li class="menu-button">
                <a href="/profile" id="profile-image"></a>
            </li>
            <form class="search-box" action="/search">
                <span id="search-icon"></span>
                <input class="search-input" type="text" name="q" placeholder=" Search..." autocomplete="off" dir="ltr"/>
            </form>
        </ul>
    </div>
</div>